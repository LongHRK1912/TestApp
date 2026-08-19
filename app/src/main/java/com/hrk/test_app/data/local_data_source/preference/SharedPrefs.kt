package com.hrk.test_app.data.local_data_source.preference

import android.content.Context
import com.hrk.test_app.common.Constant.LANGUAGE_KEY
import com.hrk.test_app.common.Constant.THEME_KEY
import com.hrk.test_app.utils.Empty
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefs @Inject constructor(
    @ApplicationContext app: Context
) : SharePreference(app) {
    var currentLanguage: String by SharedPreferenceProperty(LANGUAGE_KEY, String.Empty)
    var currentTheme: Int by SharedPreferenceProperty(THEME_KEY, -1)
}

