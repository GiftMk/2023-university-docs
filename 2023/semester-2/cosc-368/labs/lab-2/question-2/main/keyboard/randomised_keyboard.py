from string import ascii_lowercase
from random import shuffle
from tkinter import *
from abstract_keyboard import AbstractKeyboard


class RandomisedKeyboard(AbstractKeyboard):

    def __init__(self):
        self.button_size_px = 64
        self.on_click = None
        self.keys = []

    def set_on_click(self, on_click):
        self.on_click = on_click

    def update_board(self):
        flattened_board = [char for row in self.__get_board() for char in row]
        for key, char in zip(self.keys, flattened_board):
            key.set(char)

    def __get_board(self):
        alphabet = list(ascii_lowercase)
        shuffle(alphabet)
        return [alphabet[:10], alphabet[10:19], alphabet[19:]]

    def draw(self, frame):
        keyboard_frame = Frame(
            frame,
            borderwidth=2,
            relief='raised'
        )
        keyboard_frame.grid(sticky='nesw')

        for i, row in enumerate(self.__get_board()):
            keyboard_row = Frame(keyboard_frame)
            keyboard_row_padding_x = i * self.button_size_px / 2
            keyboard_row.grid(row=i, padx=keyboard_row_padding_x, sticky='w')

            for j, char in enumerate(row):
                self.__draw_row_buttons(keyboard_row, i, j, char)

    def __draw_row_buttons(self, frame, row, col, char):
        key = StringVar()
        key.set(char)
        self.keys.append(key)

        button_frame = Frame(
            frame,
            height=self.button_size_px,
            width=self.button_size_px
        )
        button_frame.pack_propagate(False)
        button_frame.grid(row=row, column=col)
        button = Button(
            button_frame,
            textvariable=key,
            command=lambda x=key: self.on_click(x.get())
        )
        button.pack(fill=BOTH, expand=1)