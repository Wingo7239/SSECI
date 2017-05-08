package com.yw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yw.model.Quote;

public interface QuoteRepository extends JpaRepository<Quote,Integer>{

	@Query("from Quote where date >= :start and date <= :end")
	List<Quote> findByDateInterval(@Param("start")int start, @Param("end")int end);
}
