package com.studentAssist.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.Log;

@Repository
@Transactional
public class LoggingDAO extends AbstractDao {

	public void insertException(Log log) {

		persist(log);

	}

}
