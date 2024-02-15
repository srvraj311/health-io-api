package com.srvraj311.healthioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseMap extends HashMap<String, Object > {
}
