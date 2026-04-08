package com.pokecapsule.util

import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object BallNbt {

    private const val KEY_ROOT    = "PokeCapsuleData"
    private const val KEY_MON     = "Pokemon"
    private const val KEY_NAME    = "Name"
    private const val KEY_LEVEL   = "Level"
    private const val KEY_NATURE  = "Nature"
    private const val KEY_TYPE1   = "Type1"
    private const val KEY_TYPE2   = "Type2"
    private const val KEY_GENDER  = "Gender"
    private const val KEY_SHINY   = "Shiny"

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    fun hasPokemon(stack: ItemStack): Boolean =
        stack.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt()?.getCompound(KEY_ROOT)?.contains(KEY_MON) == true

    private fun root(stack: ItemStack): NbtCompound? =
        stack.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt()?.getCompound(KEY_ROOT)?.takeIf { !it.isEmpty }

    fun getCachedName(stack: ItemStack):   String = root(stack)?.getString(KEY_NAME)   ?: "???"
    fun getCachedLevel(stack: ItemStack):  Int    = root(stack)?.getInt(KEY_LEVEL)     ?: 0
    fun getCachedNature(stack: ItemStack): String = root(stack)?.getString(KEY_NATURE) ?: "???"
    fun getCachedType1(stack: ItemStack):  String = root(stack)?.getString(KEY_TYPE1)  ?: "???"
    fun getCachedType2(stack: ItemStack):  String = root(stack)?.getString(KEY_TYPE2)  ?: ""
    fun getCachedGender(stack: ItemStack): String  = root(stack)?.getString(KEY_GENDER) ?: ""
    fun getCachedShiny(stack: ItemStack):  Boolean = root(stack)?.getBoolean(KEY_SHINY)  ?: false

    // ------------------------------------------------------------------
    // Write
    // ------------------------------------------------------------------

    fun writePokemon(stack: ItemStack, pokemon: Pokemon, registryManager: DynamicRegistryManager) {
        val customNbt = stack.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: NbtCompound()
        val root = NbtCompound()

        // saveToNBT returns the filled compound — use the return value
        val monNbt = pokemon.saveToNBT(registryManager, NbtCompound())
        root.put(KEY_MON, monNbt)
        root.putString(KEY_NAME,   pokemon.species.name)
        root.putInt(KEY_LEVEL,     pokemon.level)
        root.putString(KEY_NATURE, pokemon.nature.displayName)
        root.putString(KEY_TYPE1,  pokemon.primaryType.displayName.string)
        root.putString(KEY_TYPE2,  pokemon.secondaryType?.displayName?.string ?: "")
        root.putBoolean(KEY_SHINY, pokemon.shiny)
        root.putString(KEY_GENDER, when (pokemon.gender) {
            Gender.MALE      -> "♂"
            Gender.FEMALE    -> "♀"
            Gender.GENDERLESS -> ""
            else             -> ""
        })

        customNbt.put(KEY_ROOT, root)
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customNbt))

        // Set a human-readable item name (non-italic)
        val genderSymbol = root.getString(KEY_GENDER).let { if (it.isNotEmpty()) " $it" else "" }
        val itemName = Text.literal("")
            .append(pokemon.getDisplayName(true))
            .append(Text.literal("$genderSymbol (Lv.${pokemon.level})").formatted(Formatting.GRAY))
            .styled { it.withItalic(false) }
        stack.set(DataComponentTypes.CUSTOM_NAME, itemName)
    }

    // ------------------------------------------------------------------
    // Read
    // ------------------------------------------------------------------

    fun readPokemon(stack: ItemStack, registryManager: DynamicRegistryManager): Pokemon? {
        val monNbt = root(stack)?.getCompound(KEY_MON)?.takeIf { !it.isEmpty } ?: return null
        return try {
            Pokemon().loadFromNBT(registryManager, monNbt)
        } catch (e: Exception) {
            null
        }
    }

    // ------------------------------------------------------------------
    // Clear (restore plain ball)
    // ------------------------------------------------------------------

    fun clear(stack: ItemStack) {
        val customNbt = stack.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return
        customNbt.remove(KEY_ROOT)
        if (customNbt.isEmpty) {
            stack.remove(DataComponentTypes.CUSTOM_DATA)
        } else {
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customNbt))
        }
        stack.remove(DataComponentTypes.CUSTOM_NAME)
    }
}
