import csv
import os
import time
from tkinter import *
from abstract_game import AbstractGame
from ..controller.abstract_controller import AbstractController
from ..keyboard.abstract_keyboard import AbstractKeyboard
from ..label.abstract_label import AbstractLabel


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
        self.filename = f"experiment_{mode}_log.txt"
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
            writer = csv.writer(file, delimiter=" ")
            writer.writerow([
                self.name,
                self.mode,
                self.controller.get_text(),
                self.controller.get_current_block(),
                total_time
            ])
