package com.payflow.audit_service.repository;

import com.payflow.audit_service.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByTransactionId(Long transactionId );

    List<AuditLog> findAllBySenderId(Long senderId);
}
