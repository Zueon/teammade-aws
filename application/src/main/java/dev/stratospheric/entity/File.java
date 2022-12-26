package dev.stratospheric.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="files")
public class File extends BaseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String name;

    private String type;

    // large object 데이터를 저장하기 위한 어노테이션  blob : 바이너리 데이터 clob : 텍스트 데이터
    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    private Project project;


    public File(String fileName, String contentType, byte[] bytes, Project project) {
        this.name = fileName;
        this.type = contentType;
        this.data = bytes;
        this.project = project;


    }
}
