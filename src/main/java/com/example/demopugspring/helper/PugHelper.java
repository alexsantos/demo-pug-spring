package com.example.demopugspring.helper;

import com.example.demopugspring.model.Mapper;

import java.util.List;

public class PugHelper {
    public boolean exists(List<Mapper> list, Long value) {
        for (Mapper mapper : list) {
            if (mapper.getId() == value) {
                return true;
            }
        }
        return false;
    }
}
