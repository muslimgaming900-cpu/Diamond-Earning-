package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.remote.FreeFireApiService
import com.example.data.repository.RewardRepository
import com.example.service.NotificationHelper
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var context: Context
  private lateinit var database: AppDatabase
  private lateinit var repository: RewardRepository
  private lateinit var userPrefs: UserPreferences

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
    context.getSharedPreferences("diamond_rewards_prefs", Context.MODE_PRIVATE).edit().clear().commit()
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    userPrefs = UserPreferences(context)
    val apiService = FreeFireApiService.create()
    val notificationHelper = NotificationHelper(context)
    repository = RewardRepository(
        transactionDao = database.transactionDao(),
        userPreferences = userPrefs,
        apiService = apiService,
        notificationHelper = notificationHelper
    )
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun `read string from context`() {
    val appName = context.getString(R.string.app_name)
    assertEquals("Diamond Rewards", appName)
  }

  @Test
  fun `ad and captcha grants exactly 10 diamonds`() = runBlocking {
    val startDiamonds = repository.userAccount.value.diamonds
    val adReward = repository.rewardAdWatched()
    assertEquals(10, adReward)
    assertEquals(startDiamonds + 10, repository.userAccount.value.diamonds)

    val captchaReward = repository.rewardCaptchaSolved()
    assertEquals(10, captchaReward)
    assertEquals(startDiamonds + 20, repository.userAccount.value.diamonds)
  }

  @Test
  fun `promo code MG69 awards 1000 diamonds and cannot be reused`() = runBlocking {
    val initialBalance = repository.userAccount.value.diamonds
    val result = repository.redeemPromoCode("MG69")
    assertTrue(result.isSuccess)
    assertEquals(initialBalance + 1000, repository.userAccount.value.diamonds)

    val secondResult = repository.redeemPromoCode("MG69")
    assertFalse(secondResult.isSuccess)
  }

  @Test
  fun `free fire 1 to 1 redemption processes securely and reduces balance`() = runBlocking {
    // Add diamonds with promo code first
    repository.redeemPromoCode("MG69")
    val balanceBefore = repository.userAccount.value.diamonds

    val redeemResult = repository.redeemFreeFireDiamonds("1083921820", 100)
    assertTrue(redeemResult.isSuccess)
    assertEquals(100, redeemResult.deliveredAmount)
    assertEquals(balanceBefore - 100, repository.userAccount.value.diamonds)
  }
}

