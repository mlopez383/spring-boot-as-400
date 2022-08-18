package com.example.as400connector.repository.impl;

import com.example.as400connector.repository.PersonRepository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("psdb2TransactionManager")
public class PersonRepositoryImpl implements PersonRepository {

  private static final String STORED_PROCEDURE_NAME = "USERT8882.getPersonById";
  private static final String SUCCESS_CODE = "00000";
  private final EntityManager em;
  private static final Logger LOG = LoggerFactory.getLogger(PersonRepositoryImpl.class);

  public PersonRepositoryImpl(@Qualifier("psdb2EntityManagerFactory") EntityManager em) {
    this.em = em;
  }

  @Override
  public String getPersonById(Integer id) {
    StoredProcedureQuery spq = getStoredProcedureQuery(id);
    if (spq.execute()) {
      String returnCode = (String) spq.getOutputParameterValue(2);
      if (SUCCESS_CODE.equalsIgnoreCase(returnCode) && !spq.getResultList().isEmpty()) {
        return (String) spq.getResultList().get(0);
      }
    } else {
      LOG.error("Received an error from the stored procedure");
    }
    return null;
  }

  private StoredProcedureQuery getStoredProcedureQuery(final Integer vendorId) {
    StoredProcedureQuery spq = em.createStoredProcedureQuery(STORED_PROCEDURE_NAME);
    spq.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
    spq.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
    spq.setParameter(1, vendorId);
    return spq;
  }
}
