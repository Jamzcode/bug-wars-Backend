package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.BugParserRequest;
import com.bugwarsBackend.bugwars.parser.BugParser;
import com.bugwarsBackend.bugwars.parser.BugParserException;
import com.bugwarsBackend.bugwars.parser.BugParserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BugParserServiceTest {

    @Mock
    private BugParserFactory bugParserFactory;

    @Mock
    private BugParser bugParser;

    @InjectMocks
    private BugParserService bugParserService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParse_Successful() throws BugParserException, BugParserException {
        String code = "sample code";
        BugParserRequest request = new BugParserRequest(code);
        List<Integer> expectedResult = Arrays.asList(1, 2, 3);

        when(bugParserFactory.createInstance()).thenReturn(bugParser);
        when(bugParser.parse(code)).thenReturn(expectedResult);

        List<Integer> result = bugParserService.parse(request);

        assertEquals(expectedResult, result);

        verify(bugParserFactory).createInstance();
        verify(bugParser).parse(code);
    }

    @Test
    public void testParse_Exception() throws BugParserException {
        String code = "sample code";
        BugParserRequest request = new BugParserRequest(code);
        BugParserException exception = new BugParserException("Parsing error");

        when(bugParserFactory.createInstance()).thenReturn(bugParser);
        when(bugParser.parse(code)).thenThrow(exception);

        try {
            bugParserService.parse(request);
        } catch (ResponseStatusException e) {
            assert e.getStatusCode().value() == HttpStatus.UNPROCESSABLE_ENTITY.value();
            String actualMessage = "";
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                actualMessage = matcher.group(1);
            }
            assertEquals("Parsing error", actualMessage);


        }

        verify(bugParserFactory).createInstance();
        verify(bugParser).parse(code);
    }
}
