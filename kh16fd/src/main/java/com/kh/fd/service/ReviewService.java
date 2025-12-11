package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.ReviewDao;
import com.kh.fd.dto.ReviewDto;
import com.kh.fd.error.TargetNotFoundException;

@Service
public class ReviewService {

	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private AttachmentService attachmentService;

	@Transactional
	public void insertReview(ReviewDto reviewDto, MultipartFile attach) {
		Integer attachmentNo = null;

		if (attach != null && !attach.isEmpty()) {
			try {
				attachmentNo = attachmentService.save(attach);
				reviewDto.setReviewAttachmentNo(attachmentNo);
			} catch (Exception e) {
				throw new RuntimeException("첨부 파일 저장에 실패했습니다. 리뷰 등록이 취소됩니다");
			}
		}
		reviewDao.insert(reviewDto);
	}

	@Transactional
	public boolean updateReview(ReviewDto reviewDto, MultipartFile newAttach) {
		ReviewDto existingReview = reviewDao.selectOne(reviewDto.getReviewNo());
		if (existingReview == null) {
			throw new TargetNotFoundException("수정 대상 리뷰가 존재하지 않습니다.");
		}
		Integer oldAttachmentNo = existingReview.getReviewAttachmentNo();
		Integer finalAttachmentNo = oldAttachmentNo;

		try {
			if (newAttach != null && !newAttach.isEmpty()) {
				if (oldAttachmentNo != null) {
					attachmentService.delete(oldAttachmentNo);
				}

				finalAttachmentNo = attachmentService.save(newAttach);
			}
			// 2-2. 새 파일이 없고, 기존 파일이 있었는데 삭제 요청이 온 경우
			// (Controller에서 넘어온 reviewDto의 reviewAttachmentNo가 null이라고 가정)
			else if (oldAttachmentNo != null && reviewDto.getReviewAttachmentNo() == null) {
				attachmentService.delete(oldAttachmentNo);
				finalAttachmentNo = null; // DB에 NULL 저장 준비
			}
			// 2-3. 내용만 수정하거나, 이미지 없는 상태 유지 (finalAttachmentNo는 oldAttachmentNo 유지)

			// DTO에 최종 파일 번호 설정 (finalAttachmentNo가 null이면 DB에 NULL 저장)
			reviewDto.setReviewAttachmentNo(finalAttachmentNo);

			// 3. 리뷰 정보 업데이트
			return reviewDao.update(reviewDto);

		} catch (TargetNotFoundException e) {
			// 이 예외는 위에서 이미 처리되었으나, 혹시 delete/save 내부에서 발생할 경우를 대비
			throw e;
		} catch (Exception e) {
			// 기타 오류 발생 시 롤백
			throw new RuntimeException("리뷰 수정 및 파일 처리 중 오류 발생", e);
		}
	}

	@Transactional
	public boolean deleteReview(int reviewNo) {

		// 1. 리뷰 조회 (파일 번호를 얻기 위해 필수)
		ReviewDto existingReview = reviewDao.selectOne(reviewNo);
		if (existingReview == null) {
			throw new TargetNotFoundException("삭제할 리뷰를 찾을 수 없습니다.");
		}

		Integer attachmentNo = existingReview.getReviewAttachmentNo();

		try {
			// 2. 리뷰 DB 레코드 삭제
			boolean reviewDeleted = reviewDao.delete(reviewNo);

			// 3. 첨부 파일이 있었다면 삭제
			if (attachmentNo != null) {
				attachmentService.delete(attachmentNo);
			}

			return reviewDeleted;

		} catch (Exception e) {
			throw new RuntimeException("리뷰 삭제 및 파일 처리 중 오류 발생", e);
		}
	}
}
