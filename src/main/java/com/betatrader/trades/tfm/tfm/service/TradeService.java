package com.betatrader.trades.tfm.tfm.service;

import com.betatrader.trades.tfm.tfm.entity.Trade;
import com.betatrader.trades.tfm.tfm.repo.TradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> findAllTrades(){
        return tradeRepository.findAll();
    }

    public Page<Trade> getTrades(Pageable pageable){
        return tradeRepository.findAll(pageable);
    }

    @Transactional
    public Trade save(Trade trade){
        return tradeRepository.save(trade);
    }
}
