package dev.stratospheric.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
public class Resume  extends BaseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String rid;

    private String name;

    private String type;

    // large object 데이터를 저장하기 위한 어노테이션  blob : 바이너리 데이터 clob : 텍스트 데이터
    @Lob
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_mid")
    private Member creator;

    public Resume(String fileName, String contentType, byte[] bytes, Member creator) {
        this.name=fileName;
        this.type = contentType;
        this.data=bytes;
        this.creator=creator;
    }

    public Resume() {

    }
}
