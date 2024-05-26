package ch.hslu.mobpro.diabetes.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.hslu.mobpro.diabetes.data.database.entity.Product
import ch.hslu.mobpro.diabetes.data.database.dao.ProductDAO
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTestProductDB {

    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDAO
    @Before
    fun createDB() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        productDao = db.productDao()
    }

    @Test
    fun testInsertProduct() {

        val product = Product("Banane", 20.0f)
        productDao.insertProduct(product)

        val allProducts = productDao.getAll()
        assertEquals(1, allProducts.size)
        assertEquals("Banane", allProducts[0].name)
        assertEquals(20.0f, allProducts[0].carbs)
    }

    @Test
    fun testDeleteProduct() {

        val product = Product("Banane", 20.0f)
        val product1 = Product("Weissbrot", 50.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)

        var allProducts = productDao.getAll()
        productDao.deleteProduct(allProducts[1])
        allProducts = productDao.getAll()
        assertEquals(1, allProducts.size)
    }
    @Test
    fun testDeleteProductByName() {

        val product = Product("Banane", 20.0f)
        val product1 = Product("Weissbrot", 50.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)

        productDao.deleteProductByName(product1.name!!)
        val allProducts = productDao.getAll()
        assertEquals(1, allProducts.size)
    }

    @Test
    fun testUpdateProduct() {

        val product = Product("Banane", 20.0f)
        productDao.insertProduct(product)

        val allProducts = productDao.getAll()
        allProducts[0].carbs = 30.0f
        productDao.updateProduct(allProducts[0])
        assertEquals(1, allProducts.size)
        assertEquals("Banane", allProducts[0].name!!)
        assertEquals(30.0f, allProducts[0].carbs)
    }
    @Test
    fun testFindProductFuzzy() {

        val product = Product("Banane", 20.0f)
        val product1 = Product("Berry", 10.0f)
        val product2 = Product("Ananas", 30.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)
        productDao.insertProduct(product2)

        val foundProducts = productDao.findProductsFuzzy("B")
        assertEquals(2, foundProducts.size)
    }

    @Test
    fun testGetProductByName() {

        val product = Product("Banane", 20.0f)
        val product1 = Product("Berry", 10.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)

        val retrievedProduct = productDao.getProductByName("Banane")
        assertEquals("Banane", retrievedProduct!!.name)
        assertEquals(20.0f, retrievedProduct.carbs)
    }

    @Test
    fun testGetOrdered() {

        val product = Product("ag", 20.0f)
        val product1 = Product("ab", 10.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)

        val allProducts = productDao.getAllOrdered()
        assertEquals("ab", allProducts[0].name)
        assertEquals("ag", allProducts[1].name)
    }

    @Test
    fun testFindProductsFuzzyOrdered() {

        val product = Product("ag", 20.0f)
        val product1 = Product("bg", 20.0f)
        val product2 = Product("ab", 10.0f)
        productDao.insertProduct(product)
        productDao.insertProduct(product1)
        productDao.insertProduct(product2)

        val allProducts = productDao.findProductsFuzzyOrdered("a")
        assertEquals("ab", allProducts[0].name)
        assertEquals("ag", allProducts[1].name)
    }
}