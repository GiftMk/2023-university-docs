from tkinter import *
from controller.target_letter_controller import TargetLetterController
from label.target_letter_label import TargetLetterLabel
from keyboard.randomised_keyboard import RandomisedKeyboard
from game.game import Game

BOARD = ['qwertyuiop', 'asdfghjkl', 'zxcvbnm']
NUM_BLOCKS = 6
BLOCK_SIZE = 6
MODE = 'dynamic'
NAME = 'Andy'


def main():
    window = Tk()
    controller = TargetLetterController(NUM_BLOCKS, BLOCK_SIZE)
    label = TargetLetterLabel()
    keyboard = RandomisedKeyboard()
    game = Game(window, controller, label, keyboard, MODE, NAME)
    game.launch()


if __name__ == "__main__":
    main()
