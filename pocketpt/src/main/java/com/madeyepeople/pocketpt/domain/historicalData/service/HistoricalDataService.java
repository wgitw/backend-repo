package com.madeyepeople.pocketpt.domain.historicalData.service;

import com.madeyepeople.pocketpt.domain.account.constant.Role;
import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.account.repository.AccountRepository;
import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.dto.request.*;
import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataFileResponse;
import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataResponse;
import com.madeyepeople.pocketpt.domain.historicalData.dto.response.HistoricalDataUpdateResponse;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalDataFile;
import com.madeyepeople.pocketpt.domain.historicalData.mapper.ToHistoricalDataEntityMapper;
import com.madeyepeople.pocketpt.domain.historicalData.mapper.ToHistoricalDataFileEntityMapper;
import com.madeyepeople.pocketpt.domain.historicalData.mapper.ToHistoricalDataFileResponseMapper;
import com.madeyepeople.pocketpt.domain.historicalData.mapper.ToHistoricalDataResponseMapper;
import com.madeyepeople.pocketpt.domain.historicalData.repository.HistoricalDataFileRepository;
import com.madeyepeople.pocketpt.domain.historicalData.repository.HistoricalDataRepository;
import com.madeyepeople.pocketpt.domain.ptMatching.constant.PtStatus;
import com.madeyepeople.pocketpt.domain.ptMatching.entity.PtMatching;
import com.madeyepeople.pocketpt.domain.ptMatching.repository.PtMatchingRepository;
import com.madeyepeople.pocketpt.global.common.CommonFunction;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import com.madeyepeople.pocketpt.global.result.ResultCode;
import com.madeyepeople.pocketpt.global.result.ResultResponse;
import com.madeyepeople.pocketpt.global.s3.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoricalDataService {
    private final HistoricalDataRepository historicalDataRepository;
    private final ToHistoricalDataEntityMapper toHistoricalDataEntityMapper;
    private final ToHistoricalDataResponseMapper toHistoricalDataResponseMapper;
    private final S3FileService s3FileService;
    private final HistoricalDataFileRepository historicalDataFileRepository;
    private final ToHistoricalDataFileEntityMapper toHistoricalDataFileEntityMapper;
    private final ToHistoricalDataFileResponseMapper toHistoricalDataFileResponseMapper;
    private final AccountRepository accountRepository;
    private final PtMatchingRepository ptMatchingRepository;

    private final String uploadPath = "historical-data-file/";

    @Transactional
    public ResultResponse createHistoricalData(Account account, HistoricalDataCreateRequest historicalDataCreateRequest) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalData] START");

        // [1] HistoricalData 생성
        HistoricalData historicalData = toHistoricalDataEntityMapper.of(account, historicalDataCreateRequest);
        HistoricalData savedHistoricalData = historicalDataRepository.save(historicalData);
        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalData] savedHistoricalData>> {}", savedHistoricalData);

        // [2] S3에 이미지 업로드 및 저장
        List<HistoricalDataFileResponse> historicalDataFileResponseList = new ArrayList<>();
        for(MultipartFile file: historicalDataCreateRequest.getFile()) {
            String fileUrl = s3FileService.uploadFile(uploadPath + account.getAccountId() + "/", file);
            HistoricalDataFile historicalDataFile = toHistoricalDataFileEntityMapper.of(historicalData, historicalDataCreateRequest, fileUrl);
            HistoricalDataFile savedHistoricalDataFile = historicalDataFileRepository.save(historicalDataFile);
            log.info("HISTORICAL-DATA-SERVICE: [createHistoricalData] savedHistoricalDataFile>> {}", savedHistoricalDataFile);
            historicalDataFileResponseList.add(toHistoricalDataFileResponseMapper.of(savedHistoricalDataFile));
        }

        // [3] HistoricalDataResponse 생성
        HistoricalDataResponse historicalDataResponse = toHistoricalDataResponseMapper.of(savedHistoricalData, historicalDataFileResponseList);
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_CREATE_SUCCESS, historicalDataResponse);

        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalData] END");
        log.info("=======================");
        return resultResponse;
    }

    @Transactional
    public ResultResponse createHistoricalDataFile(Account account, Long historicalDataId, HistoricalDataFileCreateRequest historicalDataFileCreateRequest) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalDataFile] START");

        // [1] HistoricalData 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalData historicalData = historicalDataRepository.findByHistoricalDataIdAndAccountAndIsDeletedFalse(historicalDataId, account)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORICAL_DATA_NOT_FOUND, CustomExceptionMessage.HISTORICAL_DATA_NOT_FOUND.getMessage())
        );

        // [2] S3에 이미지 업로드 및 저장
        String fileUrl = s3FileService.uploadFile(uploadPath + account.getAccountId() + "/", historicalDataFileCreateRequest.getFile());
        HistoricalDataFile historicalDataFile = toHistoricalDataFileEntityMapper.of(historicalData, historicalDataFileCreateRequest, fileUrl);
        HistoricalDataFile savedHistoricalDataFile = historicalDataFileRepository.save(historicalDataFile);
        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalDataFile] savedHistoricalDataFile>> {}", savedHistoricalDataFile);

        // [4] HistoricalDataFileResponse 생성
        HistoricalDataFileResponse historicalDataFileResponse = toHistoricalDataFileResponseMapper.of(savedHistoricalDataFile);
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_FILE_CREATE_SUCCESS, historicalDataFileResponse);

        log.info("HISTORICAL-DATA-SERVICE: [createHistoricalDataFile] END");
        log.info("=======================");
        return resultResponse;
    }

    // 트레이니 조회 - 본인 사진 모두 조회
    @Transactional
    public ResultResponse getHistoricalDataListForTrainee(Account account, String startDate, String endDate) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [getHistoricalDataListForTrainee] START");

        // [1] 본인이 올린 데이터 전체 조회
        Date start = CommonFunction.convertStringToDate(startDate);
        Date end = CommonFunction.convertStringToDate(endDate);
        List<HistoricalData> historicalDataList = historicalDataRepository.findByAccountAndIsDeletedFalseAndDateBetween(account, start, end);

        // [2] HistoricalDataFile 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        // HistoricalData와 HistoricalDataFile은 1:N 관계로 매핑
        List<HistoricalDataResponse> historicalDataResponseList = new ArrayList<>();
        for(HistoricalData historicalData : historicalDataList) {
            List<HistoricalDataFile> historicalDataFileList = historicalDataFileRepository.findByHistoricalDataAndIsDeletedFalse(historicalData);
            List<HistoricalDataFileResponse> historicalDataFileResponseList = new ArrayList<>();
            for(HistoricalDataFile historicalDataFile: historicalDataFileList) {
                historicalDataFileResponseList.add(toHistoricalDataFileResponseMapper.of(historicalDataFile));
            }
            historicalDataResponseList.add(toHistoricalDataResponseMapper.of(historicalData, historicalDataFileResponseList));
        }

        // [3] HistoricalDataResponse 생성
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_GET_SUCCESS, historicalDataResponseList);

        log.info("HISTORICAL-DATA-SERVICE: [getHistoricalDataListForTrainee] END");
        log.info("=======================");

        return resultResponse;
    }

    // 트레이너 조회 - 사진 모두 조회 but, 회원이 public이라고 설정한 것만 조회 가능
    // TODO: 특정 기간에 대한 조건 추가할 것
    @Transactional
    public ResultResponse getHistoricalDataListByTraineeForTrainer(Account trainer, Long trainerId, Long traineeId, String startDate, String endDate) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [getHistoricalDataListByTraineeForTrainer] START");

        // [1] 해당 account의 trainee가 맞는지 ptMatching 테이블에서 확인
        Account trainee = accountRepository.findByAccountIdAndIsDeletedFalse(traineeId)
                .orElseThrow( () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, CustomExceptionMessage.ACCOUNT_NOT_FOUND.getMessage())
        );
        PtMatching ptMatching = ptMatchingRepository.findByTrainerAccountIdAndTraineeAccountIdAndStatusAndIsDeletedFalse(trainer.getAccountId(), trainee.getAccountId(), PtStatus.ACTIVE)
                .orElseThrow( () -> new BusinessException(ErrorCode.PT_MATCHING_ERROR, CustomExceptionMessage.PT_MATCHING_BAD_REQUEST.getMessage())
        );

        // [2] 트레이니가 올린 데이터 중 scope이 public인 데이터 전체 조회
        Date start = CommonFunction.convertStringToDate(startDate);
        Date end = CommonFunction.convertStringToDate(endDate);
        List<HistoricalData> historicalDataList = historicalDataRepository.findByAccountAndScopeAndIsDeletedFalseAndDateBetween(trainee, Scope.PUBLIC, start, end);

        // [3] HistoricalDataFile 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        // HistoricalData와 HistoricalDataFile은 1:N 관계로 매핑
        List<HistoricalDataResponse> historicalDataResponseList = new ArrayList<>();
        for(HistoricalData historicalData : historicalDataList) {
            List<HistoricalDataFile> historicalDataFileList = historicalDataFileRepository.findByHistoricalDataAndScopeAndIsDeletedFalse(historicalData, Scope.PUBLIC);
            List<HistoricalDataFileResponse> historicalDataFileResponseList = new ArrayList<>();
            for(HistoricalDataFile historicalDataFile: historicalDataFileList) {
                historicalDataFileResponseList.add(toHistoricalDataFileResponseMapper.of(historicalDataFile));
            }
            historicalDataResponseList.add(toHistoricalDataResponseMapper.of(historicalData, historicalDataFileResponseList));
        }

        // [3] HistoricalDataResponse 생성
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_GET_SUCCESS, historicalDataResponseList);

        log.info("HISTORICAL-DATA-SERVICE: [getHistoricalDataListByTraineeForTrainer] END");
        log.info("=======================");

        return resultResponse;
    }

    // TODO: 단일 데이터 조회


    // Historical Data 수정 - title, description, scope
    @Transactional
    public ResultResponse updateHistoricalData(Account account, Long historicalDataId, HistoricalDataUpdateRequest historicalDataUpdateRequest) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [updateHistoricalData] START");

        // [1] HistoricalData 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalData historicalData = historicalDataRepository.findByHistoricalDataIdAndAccountAndIsDeletedFalse(historicalDataId, account)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORICAL_DATA_NOT_FOUND, CustomExceptionMessage.HISTORICAL_DATA_NOT_FOUND.getMessage())
        );

        // [2] HistoricalData 수정
        HistoricalData updatedHistoricalData = historicalData.updateTitleAndDescriptionAndScope(
                CommonFunction.convertStringToDate(historicalDataUpdateRequest.getDate()),
                historicalDataUpdateRequest.getTitle(),
                historicalDataUpdateRequest.getDescription(),
                historicalDataUpdateRequest.getScope()
        );
        HistoricalData savedHistoricalData = historicalDataRepository.save(updatedHistoricalData);
        log.info("HISTORICAL-DATA-SERVICE: [updateHistoricalData] savedHistoricalData>> {}", savedHistoricalData);

        // [3] HistoricalData Response 생성
        HistoricalDataUpdateResponse historicalDataUpdateResponse = toHistoricalDataResponseMapper.of(savedHistoricalData);
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_UPDATE_SUCCESS, historicalDataUpdateResponse);

        log.info("HISTORICAL-DATA-SERVICE: [updateHistoricalData] END");
        log.info("=======================");

        return resultResponse;
    }

    // Historical Data File 수정 - scope
    @Transactional
    public ResultResponse updateHistoricalDataFile(Account account, Long historicalDataId, Long historicalDataFileId, HistoricalDataFileUpdateRequest historicalDataFileUpdateRequest) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [updateHistoricalDataFile] START");

        // [1] HistoricalData 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalData historicalData = historicalDataRepository.findByHistoricalDataIdAndAccountAndIsDeletedFalse(historicalDataId, account)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORICAL_DATA_NOT_FOUND, CustomExceptionMessage.HISTORICAL_DATA_NOT_FOUND.getMessage())
        );

        // [2] HistoricalDataFile 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalDataFile historicalDataFile = historicalDataFileRepository.findByHistoricalDataAndHistoricalDataFileIdAndIsDeletedFalse(historicalData, historicalDataFileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HISTORY_DATA_FILE_NOT_FOUND, CustomExceptionMessage.HISTORY_DATA_FILE_NOT_FOUND.getMessage())
        );

        // [3] HistoricalDataFile 수정
        HistoricalDataFile updatedHistoricalDataFile = historicalDataFile.updateScope(historicalDataFileUpdateRequest.getScope());
        HistoricalDataFile savedHistoricalDataFile = historicalDataFileRepository.save(updatedHistoricalDataFile);

        // [4] HistoricalDataFile Response 생성
        HistoricalDataFileResponse historicalDataFileResponse = toHistoricalDataFileResponseMapper.of(savedHistoricalDataFile);
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_FILE_UPDATE_SUCCESS, historicalDataFileResponse);

        log.info("HISTORICAL-DATA-SERVICE: [updateHistoricalDataFile] END");
        log.info("=======================");

        return resultResponse;
    }

    // Historical Data 삭제
    @Transactional
    public ResultResponse deleteHistoricalData(Account account, Long historicalDataId) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [deleteHistoricalData] START");

        // [1] HistoricalData 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalData historicalData = historicalDataRepository.findByHistoricalDataIdAndAccountAndIsDeletedFalse(historicalDataId, account)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORICAL_DATA_NOT_FOUND, CustomExceptionMessage.HISTORICAL_DATA_NOT_FOUND.getMessage())
        );

        // [2] HistoricalDataFile 삭제
        List<HistoricalDataFile> historicalDataFileList = historicalDataFileRepository.findByHistoricalDataAndIsDeletedFalse(historicalData);
        for(HistoricalDataFile historicalDataFile: historicalDataFileList) {
            historicalDataFile.setIsDeleted(Boolean.TRUE);
            historicalDataFileRepository.save(historicalDataFile);
        }

        // [3] HistoricalData 삭제
        historicalData.setIsDeleted(Boolean.TRUE);
        historicalDataRepository.save(historicalData);

        // [4] ResultResponse 생성
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_DELETE_SUCCESS, "delete success");

        log.info("HISTORICAL-DATA-SERVICE: [deleteHistoricalData] END");
        log.info("=======================");

        return resultResponse;
    }

    // Historical Data File 삭제
    @Transactional
    public ResultResponse deleteHistoricalDataFile(Account account, Long historicalDataId, Long historicalDataFileId) {
        log.info("=======================");
        log.info("HISTORICAL-DATA-SERVICE: [deleteHistoricalDataFile] START");

        // [1] HistoricalData 조회 - 본인이 맞는지 확인 및 지워지지 않은 것만 조회
        HistoricalData historicalData = historicalDataRepository.findByHistoricalDataIdAndAccountAndIsDeletedFalse(historicalDataId, account)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORICAL_DATA_NOT_FOUND, CustomExceptionMessage.HISTORICAL_DATA_NOT_FOUND.getMessage())
        );

        // [2] HistoricalDataFile 삭제
        HistoricalDataFile historicalDataFile = historicalDataFileRepository.findByHistoricalDataAndHistoricalDataFileIdAndIsDeletedFalse(historicalData, historicalDataFileId)
                .orElseThrow( () -> new BusinessException(ErrorCode.HISTORY_DATA_FILE_NOT_FOUND, CustomExceptionMessage.HISTORY_DATA_FILE_NOT_FOUND.getMessage()));
        historicalDataFile.setIsDeleted(Boolean.TRUE);
        historicalDataFileRepository.save(historicalDataFile);

        // [3] ResultResponse 생성
        ResultResponse resultResponse = new ResultResponse(ResultCode.HISTORICAL_DATA_FILE_DELETE_SUCCESS, "delete success");

        log.info("HISTORICAL-DATA-SERVICE: [deleteHistoricalDataFile] END");
        log.info("=======================");

        return resultResponse;
    }
}
