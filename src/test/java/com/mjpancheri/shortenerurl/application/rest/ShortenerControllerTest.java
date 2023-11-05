package com.mjpancheri.shortenerurl.application.rest;

import com.mjpancheri.shortenerurl.application.rest.builder.ShortenerBuilder;
import com.mjpancheri.shortenerurl.application.service.ShortenerService;
import com.mjpancheri.shortenerurl.core.exception.InvalidUrlException;
import com.mjpancheri.shortenerurl.core.exception.ResourceNotFoundException;
import com.mjpancheri.shortenerurl.core.exception.ShortenerUrlExceptionHandler;
import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import com.mjpancheri.shortenerurl.core.shortener.Utils;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShortenerControllerTest {

    private static final String ENDPOINT_URL = "/api/shorteners/";

    @InjectMocks
    private ShortenerController shortenerController;
    @Mock
    private ShortenerService shortenerService;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shortenerController)
                .setControllerAdvice(new ShortenerUrlExceptionHandler())
                .build();
    }

    @Test
    void testIfcreateReturnOkWhenPayloadIsCorrect() throws Exception {
        when(shortenerService.save(any(ShortenerDto.class))).thenReturn(ShortenerBuilder.getShortener());

        String response = mockMvc.perform(
                post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.asJsonString(ShortenerBuilder.getShortenerDto()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(response.contains("/" + ShortenerBuilder.ID + "/short"));
        verify(shortenerService, times(1)).save(any(ShortenerDto.class));
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testIfcreateReturnErrorWhenPayloadIsInvalid() throws Exception {
        when(shortenerService.save(any(ShortenerDto.class))).thenThrow(new InvalidUrlException());

        mockMvc.perform(
                        post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Utils.asJsonString(ShortenerBuilder.getShortenerDto()))
                )
                .andExpect(status().isBadRequest());
        verify(shortenerService, times(1)).save(any(ShortenerDto.class));
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testGetUrlReturnOkWhenExists() throws Exception {
        when(shortenerService.getUrl(anyString(), anyString())).thenReturn(ShortenerBuilder.getShortener());

        String response = mockMvc.perform(
                        get(ENDPOINT_URL + ShortenerBuilder.ID + "/short")
                                .header("user-agent", ShortenerBuilder.USER_AGENT)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(response.contains(ShortenerBuilder.URL));
        verify(shortenerService, times(1)).getUrl(anyString(), anyString());
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testGetUrlReturnErrorWhenNotExists() throws Exception {
        when(shortenerService.getUrl(anyString(), anyString())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(
                        get(ENDPOINT_URL + ShortenerBuilder.ID + "/short")
                                .header("user-agent", ShortenerBuilder.USER_AGENT)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(shortenerService, times(1)).getUrl(anyString(), anyString());
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testGetOneReturnOkWhenExists() throws Exception {
        when(shortenerService.find(anyString())).thenReturn(ShortenerBuilder.getShortener());

        mockMvc.perform(
                        get(ENDPOINT_URL + ShortenerBuilder.ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ShortenerBuilder.ID))
                .andExpect(jsonPath("$.url").value(ShortenerBuilder.URL))
                .andExpect(jsonPath("$.count").value(ShortenerBuilder.COUNT))
                .andExpect(jsonPath("$.consumers[0].origin").value(ShortenerBuilder.USER_AGENT));
        verify(shortenerService, times(1)).find(anyString());
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testGetOneReturnErrorWhenNotExists() throws Exception {
        when(shortenerService.find(anyString())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(
                        get(ENDPOINT_URL + ShortenerBuilder.ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(shortenerService, times(1)).find(anyString());
        verifyNoMoreInteractions(shortenerService);
    }

    @Test
    void testListAll() throws Exception {
        when(shortenerService.list()).thenReturn(Collections.singletonList(ShortenerBuilder.getShortener()));

        mockMvc.perform(
                        get(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ShortenerBuilder.ID))
                .andExpect(jsonPath("$[0].url").value(ShortenerBuilder.URL))
                .andExpect(jsonPath("$[0].count").value(ShortenerBuilder.COUNT));
        verify(shortenerService, times(1)).list();
        verifyNoMoreInteractions(shortenerService);
    }
}