package com.betatrader.trades.tfm.tfm.repo;

import com.betatrader.trades.tfm.tfm.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    //public List<Trade> findAll();
}
