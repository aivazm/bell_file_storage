package com.bell.storage.dto;

import com.bell.storage.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class UsersFileDto {
    private Long id;
    private User owner;
    private String fileName;
    private byte[] fileInBytes;
    private int downloadCount;
}
