package com.cathive.sass.constraints;

import com.google.common.collect.Sets;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test cases for {@link com.cathive.sass.constraints.ScssFileValidation}
 * @author Alexander Erben
 */
public class ScssFileValidationTest {

    @Test
    public void testGetSetAllowedFileExtensions() throws Exception {
        assertThat(ScssFileValidation.getAllowedFileExtensions(), hasItem(".scss"));
        assertThat(ScssFileValidation.getAllowedFileExtensions(), hasItem(".sass"));
        ScssFileValidation.setAllowedFileExtensions(Sets.newHashSet(".sass2"));
        assertThat(Sets.newHashSet(".sass2"), equalTo(ScssFileValidation.getAllowedFileExtensions()));
    }

    @Test
    public void testScssFileFileValidation() throws Exception {
        ScssFileValidation.ScssFileFileValidator validator = new ScssFileValidation.ScssFileFileValidator();
        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
        assertTrue(validator.isValid(Files.createTempFile(null, null).toFile(), mockContext));
        assertFalse(validator.isValid(Files.createTempDirectory(null).toFile(), mockContext));
    }

    @Test
    public void testScssFilePathValidation() throws Exception {
        ScssFileValidation.ScssFilePathValidator validator = new ScssFileValidation.ScssFilePathValidator();
        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
        assertTrue(validator.isValid(Files.createTempFile(null, null), mockContext));
        assertFalse(validator.isValid(Files.createTempDirectory(null), mockContext));
    }
}