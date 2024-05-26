package ch.hslu.mobpro.diabetes.presentation.shared_viewmodels

import ch.hslu.mobpro.diabetes.domain.model.User

import ch.hslu.mobpro.diabetes.presentation.common.shared_viewmodels.UserFormViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserFormViewModelTest {

    private lateinit var userFormViewModel: UserFormViewModel

    @Before
    fun setUp() {
        userFormViewModel = UserFormViewModel(User())
    }

    @Test
    fun testUpdateUserName() {
        userFormViewModel.updateUserName("John Doe")
        assertEquals("John Doe", userFormViewModel.userProfileState.value.name.value)
        assertNull(userFormViewModel.nameError.value)
    }

    @Test
    fun testUpdateInsulinPer10gCarbs() {
        userFormViewModel.updateInsulinPer10gCarbs(1.5f)
        assertEquals(1.5f, userFormViewModel.userProfileState.value.insulinPer10gCarbs.value, 0.0f)
        assertNull(userFormViewModel.insulinPer10gCarbsError.value)
    }

    @Test
    fun testUpdateInsulinPer1mmolL() {
        userFormViewModel.updateInsulinPer1mmolL(1.5f)
        assertEquals(1.5f, userFormViewModel.userProfileState.value.insulinPer1mmolL.value, 0.0f)
        assertNull(userFormViewModel.insulinPer1mmolLError.value)
    }

    @Test
    fun testUpdateUpperBoundGlucoseLevel() {
        userFormViewModel.updateUpperBoundGlucoseLevel(5.5f)
        assertEquals(5.5f, userFormViewModel.userProfileState.value.upperBoundGlucoseLevel.value, 0.0f)
        assertNull(userFormViewModel.upperBoundGlucoseLevelError.value)
    }

    @Test
    fun testUpdateLowerBoundGlucoseLevel() {
        userFormViewModel.updateLowerBoundGlucoseLevel(3.5f)
        assertEquals(3.5f, userFormViewModel.userProfileState.value.lowerBoundGlucoseLevel.value, 0.0f)
        assertNull(userFormViewModel.lowerBoundGlucoseLevelError.value)
    }

    @Test
    fun testValidateUserName() {
        userFormViewModel.updateUserName("")
        assertEquals("Name cannot be empty", userFormViewModel.nameError.value)

        userFormViewModel.updateUserName("John Doe")
        assertNull(userFormViewModel.nameError.value)
    }

    @Test
    fun testValidateInsulinPer10gCarbs() {
        userFormViewModel.updateInsulinPer10gCarbs(0.0f)
        assertEquals("Value must be greater than 0", userFormViewModel.insulinPer10gCarbsError.value)

        userFormViewModel.updateInsulinPer10gCarbs(1.5f)
        assertNull(userFormViewModel.insulinPer10gCarbsError.value)
    }

    @Test
    fun testValidateInsulinPer1mmolL() {
        userFormViewModel.updateInsulinPer1mmolL(0.0f)
        assertEquals("Value must be greater than 0", userFormViewModel.insulinPer1mmolLError.value)

        userFormViewModel.updateInsulinPer1mmolL(1.5f)
        assertNull(userFormViewModel.insulinPer1mmolLError.value)
    }

    @Test
    fun testValidateUpperBoundGlucoseLevel() {
        userFormViewModel.updateUpperBoundGlucoseLevel(0.0f)
        assertEquals("Value must be greater than 0", userFormViewModel.upperBoundGlucoseLevelError.value)

        userFormViewModel.updateUpperBoundGlucoseLevel(5.5f)
        assertNull(userFormViewModel.upperBoundGlucoseLevelError.value)
    }

    @Test
    fun testValidateLowerBoundGlucoseLevel() {
        userFormViewModel.updateLowerBoundGlucoseLevel(0.0f)
        assertEquals("Value must be greater than 0", userFormViewModel.lowerBoundGlucoseLevelError.value)

        userFormViewModel.updateLowerBoundGlucoseLevel(3.5f)
        assertNull(userFormViewModel.lowerBoundGlucoseLevelError.value)
    }

    @Test
    fun testValidateAll() {
        userFormViewModel.updateUserName("")
        userFormViewModel.updateInsulinPer10gCarbs(0.0f)
        userFormViewModel.updateInsulinPer1mmolL(0.0f)
        userFormViewModel.updateUpperBoundGlucoseLevel(0.0f)
        userFormViewModel.updateLowerBoundGlucoseLevel(0.0f)

        assertFalse(userFormViewModel.validateAll())

        userFormViewModel.updateUserName("John Doe")
        userFormViewModel.updateInsulinPer10gCarbs(1.5f)
        userFormViewModel.updateInsulinPer1mmolL(1.5f)
        userFormViewModel.updateUpperBoundGlucoseLevel(5.5f)
        userFormViewModel.updateLowerBoundGlucoseLevel(3.5f)

        assertTrue(userFormViewModel.validateAll())
    }
}
