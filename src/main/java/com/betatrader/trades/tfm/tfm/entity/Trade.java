package com.betatrader.trades.tfm.tfm.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "trade")
@Data
@JacksonXmlRootElement(localName = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tradeId;
    private String upstreamTradeId;
    private String upstreamSystemId;
    private String status;
    private String upstreamMessageId;
    private String upstreamReplyToQueue;
    private String settlementSystemId;
    private String settlementRequestMsgId;
    private String settlementStatus;
    private String settlementReference;
    private String errorCode;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;
    private String security;
    private String account;
    private String quantity;
    private String cost;
    private String tradeType;
    // getters/setters

}