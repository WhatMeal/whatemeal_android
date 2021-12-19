package com.beside.whatmeal.main

import com.beside.whatmeal.data.SettingLocalDataSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var settingLocalDataSource: SettingLocalDataSource

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(settingLocalDataSource)
    }

    @Test
    fun `Test tutorialShownOrNull`() {
        var expected: Boolean? = null

        // Verify
        mainViewModel.tutorialShownOrNull.observeForever { result ->
            assertEquals(expected = expected, actual = result)
        }

        // Test - not past time
        expected = null
        mainViewModel.mutablePastMinimumTime.value = false

        // Test - past time & tutorial not shown
        expected = true
        whenever(settingLocalDataSource.isTutorialShown()).thenReturn(true)
        mainViewModel.mutablePastMinimumTime.value = true

        // Test - past time & tutorial shown
        expected = null
        mainViewModel.mutablePastMinimumTime.value = false
        whenever(settingLocalDataSource.isTutorialShown()).thenReturn(false)
        expected = false
        mainViewModel.mutablePastMinimumTime.value = true
    }
}