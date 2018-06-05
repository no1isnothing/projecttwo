package com.thebipolaroptimist.projecttwo.models;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.*;

public class DetailDTOTest {

    @Test
    public void constructor()
    {
        DetailDTO detailDTO = new DetailDTO();

        assertEquals(Color.BLUE, detailDTO.color);
        assertEquals("Default", detailDTO.detailType);
    }

}