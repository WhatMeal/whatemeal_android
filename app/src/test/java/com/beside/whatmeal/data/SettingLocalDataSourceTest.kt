package com.beside.whatmeal.data

import android.content.Context
import android.content.SharedPreferences
import com.beside.whatmeal.data.local.SettingLocalDataSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingLocalDataSourceTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var settingSharedPreferences: SharedPreferences

    @Mock
    lateinit var editor: SharedPreferences.Editor

    lateinit var settingLocalDataSource: SettingLocalDataSource

    @Before
    fun setUp() {
        settingLocalDataSource = spy(SettingLocalDataSource(context))

        whenever(context.getSharedPreferences(anyString(), anyInt()))
            .doReturn(settingSharedPreferences)
        whenever(settingSharedPreferences.edit()).doReturn(editor)
    }

    @Test
    fun `Test isTutorialShown`() {
        // Test
        settingLocalDataSource.isTutorialShown()

        // Verify
        verify(settingSharedPreferences).getBoolean(eq("is_tutorial_shown"), eq(false))
    }

    @Test
    fun `Test setTutorialShown`() {
        // Setup
        doNothing().whenever(editor).apply()
        whenever(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor)

        // Test
        settingLocalDataSource.setTutorialShown(true)

        // Verify
        verify(editor).putBoolean(eq("is_tutorial_shown"), eq(true))
    }
}