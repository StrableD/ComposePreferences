package com.strabled.composepreferences

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strabled.composepreferences.utilis.CategoryHeader
import com.strabled.composepreferences.utilis.Footer

/**
 * A scope for defining [PreferenceItem]s and [PreferenceCategory]s in the [PreferenceScreen].
 */
interface PreferenceScope {

    /**
     * Adds a [PreferenceItem] to the [PreferenceScreen].
     * It contains the preferences you want to define in the [PreferenceScreen].
     *
     * @param content The composable content of the preference item.
     */
    fun preferenceItem(content: @Composable PreferenceScope.() -> Unit)

    /**
     * Adds a [PreferenceCategory] with a string title to the [PreferenceScreen].
     * It can contain multiple [preferenceItem]s and [preferenceCategory]s.
     *
     * @param title The title of the preference category.
     * @param items The items within the preference category.
     */
    fun preferenceCategory(title: String, items: PreferenceScope.() -> Unit)

    /**
     * Adds a [PreferenceCategory] with a composable title to the [PreferenceScreen].
     * It can contain multiple [preferenceItem]s and [preferenceCategory]s.
     *
     * @param title The composable title of the preference category.
     * @param items The items within the preference category.
     */
    fun preferenceCategory(title: @Composable PreferenceScope.() -> Unit, items: PreferenceScope.() -> Unit)

    /**
     * Adds a [Footer] to the [PreferenceScreen].
     *
     * @param icon The composable icon to be displayed in the footer.
     * @param title The title of the footer.
     * @param onClick The action to be performed when the footer is clicked.
     * @param subtitle The optional composable subtitle to be displayed in the footer.
     */
    fun preferenceFooter(icon: @Composable () -> Unit = {}, title: String, onClick: () -> Unit = {}, subtitle: @Composable (() -> Unit)? = null)
}

/**
 * A default implementation of [PreferenceScope].
 */
internal class PreferenceScopeImpl : PreferenceScope {
    private val _preferenceItems: MutableList<PreferenceItem> = mutableListOf()
    val preferenceItemNumber: Int get() = _preferenceItems.size

    private var _preferenceFooter: PreferenceFooter? = null

    override fun preferenceItem(content: @Composable (PreferenceScope.() -> Unit)) {
        this._preferenceItems.add(
            PreferenceItem(content = {
                @Composable { content() }
            })
        )
    }

    override fun preferenceCategory(title: String, items: PreferenceScope.() -> Unit) {
        this.preferenceCategory({ CategoryHeader(title = title) }, items)
    }

    override fun preferenceCategory(title: @Composable (PreferenceScope.() -> Unit), items: PreferenceScope.() -> Unit) {
        this._preferenceItems.add(
            PreferenceCategory(
                title = { @Composable { title() } }
            )
        )

        this.apply(items)

        this._preferenceItems.add(PreferenceCategory(title = {
            @Composable {
                Spacer(modifier = Modifier.height(PreferenceTheme.spacing.preferenceScreenContentGap))
            }
        }))
    }

    override fun preferenceFooter(
        icon: @Composable (() -> Unit),
        title: String,
        onClick: () -> Unit,
        subtitle: @Composable (() -> Unit)?
    ) {
        if (_preferenceFooter != null) error("PreferenceFooter can only be set once")
        this._preferenceFooter = PreferenceFooter(content = {
            @Composable {
                Footer(
                    icon = icon,
                    title = title,
                    onClick = onClick,
                    subtitle = subtitle
                )
            }
        })
    }

    fun getFooter(): @Composable () -> Unit {
        val footer = this._preferenceFooter ?: return {}
        return footer.content.invoke(this)
    }

    fun getPreferenceItem(index: Int): @Composable () -> Unit {
        val preferenceItem = this._preferenceItems[index]
        return preferenceItem.content.invoke(this, index)
    }

    fun needsDivider(index: Int): Boolean {
        return index != this._preferenceItems.size - 1 &&
                this._preferenceItems[index] !is PreferenceCategory &&
                this._preferenceItems[index + 1] !is PreferenceCategory
    }
}

/**
 * This class represents a [PreferenceItem] in the [PreferenceScreen].
 *
 * @property content The composable content of the preference item.
 * @see PreferenceScreen
 * @see PreferenceScope.preferenceItem
 */
internal open class PreferenceItem(
    val content: PreferenceScope.(index: Int) -> @Composable () -> Unit
)

/**
 * This class represents a [PreferenceCategory] in the [PreferenceScreen].
 * It extends [PreferenceItem] and contains a composable title.
 *
 * @see PreferenceScreen
 * @see PreferenceScope.preferenceCategory
 */
internal class PreferenceCategory(
    title: PreferenceScope.(index: Int) -> @Composable () -> Unit
) : PreferenceItem(content = title)

/**
 * This class represents a [PreferenceFooter] in the [PreferenceScreen].
 * It contains the composable content of the [Footer].
 *
 * @property content The composable content of the [Footer].
 * @see PreferenceScreen
 * @see PreferenceScope.preferenceFooter
 */
internal class PreferenceFooter(
    val content: PreferenceScope.() -> @Composable () -> Unit
)