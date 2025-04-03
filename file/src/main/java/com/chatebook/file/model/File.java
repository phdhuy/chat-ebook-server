package com.chatebook.file.model;

import com.chatebook.common.model.AbstractEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "file")
public class File extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String url;

  @Column private String publicId;
}
