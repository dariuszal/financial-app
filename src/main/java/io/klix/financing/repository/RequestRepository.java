package io.klix.financing.repository;

import io.klix.financing.entity.Request;
import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("SELECT r FROM Request r WHERE r.provider = :provider AND r.requestStatus IN :status")
    List<Request> findAllPendingRequestsByProvider(@Param("provider") ProviderEnum providerEnum, @Param("status") RequestStatus statuses);

    List<Request> findAllByApplicationId(UUID applicationId);

    List<Request> findAllByRequestFinalizedIsFalseAndRequestStatus(RequestStatus requestStatus);

    Boolean existsByEmailAndRequestStatus(String email, RequestStatus requestStatus);
}
