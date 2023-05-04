package com.alfonsoalmonte.ipinformation.repository;

import com.alfonsoalmonte.ipinformation.model.IpResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpResponseRepository extends JpaRepository<IpResponse, Integer> {

}
