package com.xassure.dbControls;

import org.hibernate.Session;

import com.google.inject.Provider;
import com.xassure.dbControls.dbUtils.GetSessionFactory;

public class ControlDatabaseProvider implements Provider<DatabaseControls> {

	private static Session session;

	@Override
	public DatabaseControls get() {
		session = GetSessionFactory.getSessionFactory().openSession();
		return new DatabaseControlLibrary(session);

	}

}
