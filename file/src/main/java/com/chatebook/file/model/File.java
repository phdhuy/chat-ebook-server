package com.chatebook.file.model;

import com.chatebook.common.model.AbstractEntity;
import jakarta.persistence.*;

import java.math.BigInteger;
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
@Entity(name = "files")
public class File extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String secureUrl;

  @Column private String publicId;

  @Column private String format;

  @Column private Integer pages;

  @Column private BigInteger bytes;
}
