package com.keeper.homepage.global.util.file.server;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.keeper.homepage.global.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileServerValidatorTest {

  @Test
  @DisplayName("기존 Office MIME 타입과 확장자가 일치하면 검증을 통과한다.")
  void should_validate_when_legacyOfficeMimeMatchesExtension() {
    MockMultipartFile docFile = new MockMultipartFile(
        "file",
        "legacy.doc",
        "application/msword",
        "doc".getBytes()
    );
    MockMultipartFile xlsFile = new MockMultipartFile(
        "file",
        "legacy.xls",
        "application/vnd.ms-excel",
        "xls".getBytes()
    );
    MockMultipartFile pptFile = new MockMultipartFile(
        "file",
        "legacy.ppt",
        "application/vnd.ms-powerpoint",
        "ppt".getBytes()
    );

    FileServerValidator.validate(docFile);
    FileServerValidator.validate(xlsFile);
    FileServerValidator.validate(pptFile);
  }

  @Test
  @DisplayName("OOXML Office MIME 타입과 확장자가 일치하면 검증을 통과한다.")
  void should_validate_when_ooxmlOfficeMimeMatchesExtension() {
    MockMultipartFile docxFile = new MockMultipartFile(
        "file",
        "modern.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "docx".getBytes()
    );
    MockMultipartFile xlsxFile = new MockMultipartFile(
        "file",
        "modern.xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "xlsx".getBytes()
    );
    MockMultipartFile pptxFile = new MockMultipartFile(
        "file",
        "modern.pptx",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "pptx".getBytes()
    );

    FileServerValidator.validate(docxFile);
    FileServerValidator.validate(xlsxFile);
    FileServerValidator.validate(pptxFile);
  }

  @Test
  @DisplayName("MIME 타입과 확장자가 맞지 않으면 예외를 발생시킨다.")
  void should_throwBusinessException_when_mimeAndExtensionDoNotMatch() {
    MockMultipartFile invalidFile = new MockMultipartFile(
        "file",
        "wrong.docx",
        "application/msword",
        "docx".getBytes()
    );

    assertThatThrownBy(() -> FileServerValidator.validate(invalidFile))
        .isInstanceOf(BusinessException.class);
  }
}
