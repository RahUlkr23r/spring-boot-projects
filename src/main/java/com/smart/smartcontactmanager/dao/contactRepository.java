package com.smart.smartcontactmanager.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.entities.contact;
import java.util.List;


@Repository
public interface contactRepository extends JpaRepository<contact, Integer> {
    // You can add custom queries if needed
// @Query("from contact as d where c.user.id=:userId ")
//  public List<contact>findContactByUser(@Param ("userId")int userId);

@Query("from contact c where c.user.id = :userId")

public Page<contact> findContactByUser(@Param("userId") int userId,Pageable pePageable);

@Query("SELECT c FROM contact c WHERE c.id = :cId AND c.user.id = :userId")

Optional<contact> findByIdAndUserId (@Param("cId") Integer cId, @Param("userId") Integer userId);
List<contact> findByNameContainingIgnoreCaseAndUser(String name, User user);


}