package com.kh.fd;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.RestaurantHolidayDao;
import com.kh.fd.dao.SeatDao;
import com.kh.fd.dao.TimeSlotDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.dto.RestaurantHolidayDto;
import com.kh.fd.dto.TimeSlotDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class Test01CreatTimeSlotTest {
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private SeatDao seatDao;
	
	@Autowired
	private TimeSlotDao timeSlotDao;
	
	@Autowired
	private RestaurantHolidayDao restaurantHolidayDao;
	
	@Test
	public void Test(){
		long timeSlotTarget = 42;
		
		RestaurantDto target = restaurantDao.selectOne(timeSlotTarget);
		LocalTime open = LocalTime.parse(target.getRestaurantOpen());
	    LocalTime close = LocalTime.parse(target.getRestaurantLastOrder());
	    LocalTime breakStart = LocalTime.parse(target.getRestaurantBreakStart());
	    LocalTime breakEnd = LocalTime.parse(target.getRestaurantBreakEnd());
	    int interval = target.getReservationInterval();

	    // 자정 이후 마감이면 23:59로 조정
	    if (close.isBefore(open)) {
	        close = LocalTime.of(23, 59);
	    }

	    // 영업일
	    List<DayOfWeek> days = new ArrayList<>();
	    for (String token : target.getRestaurantOpeningDays().split(",")) {
	        switch (token.trim()) {
	            case "월": days.add(DayOfWeek.MONDAY); break;
	            case "화": days.add(DayOfWeek.TUESDAY); break;
	            case "수": days.add(DayOfWeek.WEDNESDAY); break;
	            case "목": days.add(DayOfWeek.THURSDAY); break;
	            case "금": days.add(DayOfWeek.FRIDAY); break;
	            case "토": days.add(DayOfWeek.SATURDAY); break;
	            case "일": days.add(DayOfWeek.SUNDAY); break;
	            default: throw new IllegalArgumentException("잘못된 요일: " + token);
	        }
	    }

	    // 휴무일
	    List<RestaurantHolidayDto> holidayDtoList = restaurantHolidayDao.selectList(timeSlotTarget);
	    List<LocalDate> holidayList = new ArrayList<>();
	    for (RestaurantHolidayDto dto : holidayDtoList) {
	        holidayList.add(dto.getRestaurantHolidayDate());
	    }

	    // 생성할 슬롯 리스트
	    List<TimeSlotDto> timeSlotList = new ArrayList<>();

	    LocalDate startDate = LocalDate.now();
	    LocalDate endDate = startDate.plusMonths(1);

	    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        DayOfWeek dayOfWeek = date.getDayOfWeek();

	        // 영업일 체크
	        if (!days.contains(dayOfWeek)) continue;

	        // 휴무일 체크
	        if (holidayList.contains(date)) continue;

	        LocalTime startTime = open;

	        while (!startTime.plusMinutes(interval).isAfter(close)) {
	            LocalTime endTime = startTime.plusMinutes(interval);

	            // 브레이크타임 제외
	            if (!(startTime.isBefore(breakEnd) && endTime.isAfter(breakStart))) {
	                TimeSlotDto slot = TimeSlotDto.builder()
	                		.timeSlotTarget(timeSlotTarget)
	                        .timeSlotTargetDate(date)
	                        .timeSlotStart(startTime)
	                        .timeSlotEnd(endTime)
	                        .build();
	                timeSlotList.add(slot);
	            }

	            startTime = endTime;
	        }
	    }

	    timeSlotList.forEach(System.out::println);
        System.out.println("총 슬롯 수 = " + timeSlotList.size());

	}
}
