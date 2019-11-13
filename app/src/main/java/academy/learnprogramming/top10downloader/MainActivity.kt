package academy.learnprogramming.top10downloader

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")


    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "DownloadData")
            }

            override fun doInBackground(vararg params: String?): String {
                Log.d(TAG, "doInBackGround: starts with ${params[0]}")
                val rssFeed = downloadXML(params[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                val xmlResult = StringBuilder()
                try {
                    val url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG, "downloadXML: The response code was $response")

//
//                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//                    val inputBuffer = CharArray(500)
//                    var charsREad = 0
//                    while (charsREad >= 0) {
//                        charsREad = reader.read(inputBuffer)
//                        if (charsREad > 0) {
//                            xmlResult.append(String(inputBuffer, 0, charsREad))
//                        }
//                    }
//                    reader.close()

                    //val stream = connection.inputStream
                    connection.inputStream.buffered().reader().use { xmlResult.append(it.readText()) }

                    Log.d(TAG, "Received ${xmlResult.length} bytes")

                    return xmlResult.toString()

//                } catch (e: MalformedURLException) {
//                    Log.e(TAG, "downloadXML: Invalid URL ${e.message}")
//                } catch (e: IOException){
//                    Log.e(TAG, "downloadXML: IO Exception reading data: ${e.message}")
//                } catch (e: SecurityException) {
//                    Log.e(TAG, "downloadXML: Security exception. Needs permissions? ${e.message}")
//                } catch (e: Exception) {
//                    Log.e(TAG, "Unknown error: ${e.message}")
//                }
                } catch (e: Exception) {
                    val errorMessage = when (e) {
                        is MalformedURLException -> "downLoadXML: Invalid URL ${e.message}"
                        is IOException -> "downLoadXML: IO Exception reading data: ${e.message}"
                        is SecurityException -> {
                            e.printStackTrace()
                            "downLoadXML: Security Exception. Needs permission? ${e.message}"
                        }
                        else -> "Unknown error: ${e.message}"
                    }
                }

                return ""
            }
        }
    }


}
