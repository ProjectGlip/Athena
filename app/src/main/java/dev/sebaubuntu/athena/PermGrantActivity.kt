package dev.sebaubuntu.athena

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.sebaubuntu.athena.MainActivity
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.ShizukuProvider
import rikka.shizuku.SystemServiceHelper
import java.lang.reflect.InvocationTargetException

class PermGrantActivity : AppCompatActivity(), Shizuku.OnRequestPermissionResultListener {
    private val SHIZUKU_CODE = 4551
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm_grant)

        if (!Shizuku.pingBinder()) {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setMessage(getString(R.string.dlg_noShizuku_desc))
                .setTitle(getString(R.string.dlg_noShizuku_title))
                .setPositiveButton(getString(android.R.string.ok)) { _, _ -> finish() }
                .setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return
        } else
            requestShizukuPerm()
    }

    fun requestShizukuPerm() {
        val isGranted: Boolean = if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
            checkSelfPermission(ShizukuProvider.PERMISSION) == PackageManager.PERMISSION_GRANTED
        } else {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        }
        if (isGranted) doPMGrant() else {
            if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
                requestPermissions(arrayOf(ShizukuProvider.PERMISSION), SHIZUKU_CODE)
            } else {
                Shizuku.addRequestPermissionResultListener(this)
                Shizuku.requestPermission(SHIZUKU_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            val permission = permissions[i]
            val result = grantResults[i]
            if (permission == ShizukuProvider.PERMISSION) {
                onRequestPermissionResult(requestCode, result)
            }
        }
    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        val isGranted = grantResult == PackageManager.PERMISSION_GRANTED
        //Do stuff based on the result.
        if (requestCode == SHIZUKU_CODE) {
            if (isGranted) {
                doPMGrant()
            }
        }
    }

    fun doPMGrant() {
        try {
            @SuppressLint("PrivateApi") val iPmClass =
                Class.forName("android.content.pm.IPackageManager")
            @SuppressLint("PrivateApi") val iPmStub =
                Class.forName("android.content.pm.IPackageManager\$Stub")
            val asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
            val grantRuntimePermissionMethod = iPmClass.getMethod(
                "grantRuntimePermission",
                String::class.java,
                String::class.java,
                Int::class.javaPrimitiveType
            )
            val iPmInstance = asInterfaceMethod.invoke(
                null,
                ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package"))
            )
            grantRuntimePermissionMethod.invoke(iPmInstance, "dev.sebaubuntu.athena", "android.permission.BATTERY_STATS", 0)
            val int = Intent(this, MainActivity::class.java)
            int.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(int)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}
