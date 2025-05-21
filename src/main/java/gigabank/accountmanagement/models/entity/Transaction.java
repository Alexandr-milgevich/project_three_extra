package gigabank.accountmanagement.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gigabank.accountmanagement.constants.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(name = "uuid")
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "category")
    private String category;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;
    // Необязательные поля — зависят от источника оплаты
    @Column(name = "bankName")
    private String bankName;

    @Column(name = "cardNumber")
    private String cardNumber;

    @Column(name = "merchantName")
    private String merchantName;

    @Column(name = "digitalWalletId")
    private String digitalWalletId;

    @Column(name = "merchantCategoryCode")
    private String merchantCategoryCode;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "uuid_account", referencedColumnName = "uuid")
    Account account;
}
