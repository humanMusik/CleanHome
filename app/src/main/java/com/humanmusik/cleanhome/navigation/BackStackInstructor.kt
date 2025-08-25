package com.humanmusik.cleanhome.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface BackStackInstructor {
    val instructions: MutableList<BackStackInstruction>

    fun provideInstructions(backStack: NavBackStack)
    fun learnInstructions(vararg instructions: BackStackInstruction): BackStackInstructor
}

class StandardBackStackInstructor() : BackStackInstructor {
    override val instructions: MutableList<BackStackInstruction> = mutableListOf()

    override fun provideInstructions(backStack: NavBackStack) {
        backStack.apply {
            instructions.forEach { instruction ->
                when (instruction) {
                    BackStackInstruction.Pop -> {
                        removeAt(lastIndex)
                    }

                    is BackStackInstruction.PopUntil -> {
                        for (index in backStack.indices.reversed()) {
                            if (backStack[index] == instruction.navKey) break
                            removeAt(index)
                        }
                    }

                    is BackStackInstruction.Push -> {
                        add(instruction.navKey)
                    }
                }
            }
        }

        clearInstructions()
    }

    override fun learnInstructions(vararg instructions: BackStackInstruction): StandardBackStackInstructor {
        this.instructions.addAll(instructions)
        return this
    }

    private fun clearInstructions() {
        instructions.clear()
    }
}

@Serializable
sealed interface BackStackInstruction {
    data class Push(val navKey: NavKey) : BackStackInstruction
    data object Pop : BackStackInstruction
    data class PopUntil(val navKey: NavKey) : BackStackInstruction
}