package gigabank.accountmanagement.entity;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT, PAYMENT, TRANSFER, UNKNOWN_TYPE
}
