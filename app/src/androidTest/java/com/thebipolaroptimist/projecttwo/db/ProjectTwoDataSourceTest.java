package com.thebipolaroptimist.projecttwo.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectTwoDataSourceTest {

    @Test
    public void testOpen()
    {
        Context context = InstrumentationRegistry.getTargetContext();
        ProjectTwoDataSource projectTwoDataSource = new ProjectTwoDataSource();
        projectTwoDataSource.open();
    }

    //TODO fill in test

}