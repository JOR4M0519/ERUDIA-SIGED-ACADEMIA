package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditLog1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_id_gen")
    @SequenceGenerator(name = "audit_log_id_gen", sequenceName = "audit_log_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "table_name", nullable = false, length = 20)
    private String tableName;

    @Size(max = 20)
    @NotNull
    @Column(name = "operation", nullable = false, length = 20)
    private String operation;

    @NotNull
    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @ColumnDefault("now()")
    @Column(name = "changed_at")
    private OffsetDateTime changedAt;

    @NotNull
    @Column(name = "changed_by", nullable = false)
    private Integer changedBy;

    @Size(max = 20)
    @NotNull
    @Column(name = "ip", nullable = false, length = 20)
    private String ip;

}