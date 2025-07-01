package com.toonpick.domain.member.repository;

import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.member.entity.ToonCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ToonCollectionRepository extends JpaRepository<ToonCollection, Long> {

    List<ToonCollection> findByMember(Member member);

}
