package com.example.feature_game.model

import android.graphics.Point
import com.example.core.interactor.SpaceDustUseCase
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.feature_game.tmp.SpaceViewModel
import com.example.repository.IUserRecordRepository

/**
 * It's a data class that holds all the parameters needed to create a GameView.
 * @property {IUserRecordRepository} userRecordRepository - This is the repository that will be used to
 * save the user's score.
 * @property {Random} random - A random number generator.
 * @property {Point} screenSize - The size of the screen in pixels.
 * @property {Int} playerShipType - The type of ship the player is using.
 * @property {IMeteoriteRepository} meteoriteRepository - IMeteoriteRepository - this is the repository
 * that will be used to get the meteorites.
 * @property {SpaceDustUseCase} spaceDustInteractor - SpaceDustUseCase - this is the interactor that
 * will be used to get the space dust data from the repository.
 * @property {SpaceViewModel} spaceViewModel - SpaceViewModel - this is the ViewModel that will be used
 * to communicate with the View.
 *
 * @author Mykola Ivantsov
 * @since 31 August 2022
 */
data class GameViewParams(
    val userRecordRepository: IUserRecordRepository,
    val screenSize: Point,
    val playerShipType: Int,
    val meteoriteRepository: IMeteoriteRepository,
    val spaceDustInteractor: SpaceDustUseCase,
    val spaceViewModel: SpaceViewModel,
)