import csv
import os
import time
from abc import ABC, abstractmethod
from random import randrange, shuffle
from string import ascii_lowercase
from tkinter import *
from tkinter.ttk import *

BOARD = ['qwertyuiop', 'asdfghjkl', 'zxcvbnm']
NUM_BLOCKS = 6
BLOCK_SIZE = 6
MODE = 'static'
NAME = 'Andy'


class AbstractGame(ABC):

    @abstractmethod
    def launch(self):
        pass


class AbstractDrawable(ABC):

    @abstractmethod
    def draw(self, frame: Frame):
        pass


class AbstractController(ABC):

    @abstractmethod
    def get_text(self) -> str:
        pass

    @abstractmethod
    def update_text(self) -> str:
        pass

    @abstractmethod
    def get_current_block(self) -> int:
        pass


class AbstractLabel(AbstractDrawable):

    @abstractmethod
    def set_text(self, text: str):
        pass


class AbstractKeyboard(AbstractDrawable):

    @abstractmethod
    def set_on_click(self, on_click):
        pass

    @abstractmethod
    def update_board(self):
        pass


class Game(AbstractGame):

    def __init__(
            self,
            window: Tk,
            controller: AbstractController,
            label: AbstractLabel,
            keyboard: AbstractKeyboard,
            mode: str,
            name: str
    ):
        self.window = window
        self.frame = Frame(window)
        self.controller = controller
        self.keyboard = keyboard
        self.mode = mode
        self.name = name
        self.label = label
        self.label.set_text(controller.get_text())
        self.padding_x = 10
        self.padding_y = 10
        self.start_time = 0
        self.filename = f"experiment_{mode}_log.csv"
        self.__clear_files()

    def launch(self):
        self.frame.grid(padx=self.padding_x, pady=self.padding_y)
        self.label.draw(self.frame)
        self.keyboard.draw(self.frame)

        self.keyboard.set_on_click(self.handle_button_press)

        self.window.mainloop()

    def handle_button_press(self, char):
        self.start_time = time.time()

        if char == self.controller.get_text():
            self.__log_total_time()
            self.start_time = 0

            new_target_letter = self.controller.update_text()
            if new_target_letter is None:
                self.__close()
                return
            self.label.set_text(new_target_letter)

            if self.mode == "dynamic":
                self.keyboard.update_board()

    def __close(self):
        self.window.destroy()
        new_window = Tk()
        text = f"""
        All Done!
        
        Thank you for taking the time to complete the test.
        Your results have been stored in the file:
        "{self.filename}"
        
        Made with love by Gift :)
        """
        message_label = Label(new_window, text=text, justify=LEFT)
        message_label.pack(padx=self.padding_x, pady=self.padding_y)

    def __clear_files(self):
        if os.path.exists(self.filename):
            os.remove(self.filename)

    def __log_total_time(self):
        total_time = (time.time() - self.start_time) * 1000
        with open(self.filename, "a", newline="") as file:
            writer = csv.writer(file, delimiter=",")
            writer.writerow([
                self.name,
                self.mode,
                self.controller.get_text(),
                self.controller.get_current_block(),
                total_time
            ])


class TargetLetterController(AbstractController):

    def __init__(self, num_blocks, block_size):
        self.block_size = block_size
        self.num_blocks = num_blocks
        self.current_block = 1
        self.target_letters = self.__get_target_letters()
        self.block = self.__get_next_block()
        self.target_letter = self.update_text()

    def get_text(self):
        return self.target_letter

    def update_text(self):
        if len(self.block) == 0 and self.current_block == self.num_blocks:
            return None

        i = randrange(len(self.block))
        target_letter = self.block.pop(i)

        if len(self.block) == 0 and self.current_block < self.num_blocks:
            self.current_block += 1
            self.block = self.__get_next_block()

        self.target_letter = target_letter
        return target_letter

    def get_current_block(self) -> int:
        return self.current_block

    def __get_next_block(self) -> list:
        block = list(self.target_letters)
        shuffle(block)
        return block

    def __get_target_letters(self) -> list:
        alphabet = list(ascii_lowercase)
        shuffle(alphabet)
        return alphabet[:self.block_size]


class TargetLetterLabel(AbstractLabel):

    def __init__(self):
        self.target_letter = StringVar()
        self.padding_y = 20

    def draw(self, frame):
        label = Label(frame, textvariable=self.target_letter)
        label.grid(row=0, column=0, pady=self.padding_y)

    def set_text(self, char):
        self.target_letter.set(char)


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


def main():
    window = Tk()
    controller = TargetLetterController(NUM_BLOCKS, BLOCK_SIZE)
    label = TargetLetterLabel()
    keyboard = RandomisedKeyboard()
    game = Game(window, controller, label, keyboard, MODE, NAME)
    game.launch()


if __name__ == "__main__":
    main()
