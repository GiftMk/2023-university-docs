from tkinter import *
from collections import namedtuple
from itertools import product
from random import shuffle
import time
import csv
import os

NAME = "Andy"
LOG_FILENAME = "experiment_log.csv"

NUM_REPS = 8
DISTANCES = [64, 128, 256, 512]
WIDTHS = [8, 16, 32]

LAYOUT_WIDTH = 700
LAYOUT_HEIGHT = 700

BAR_WIDTH = 50
BAR_HEIGHT = LAYOUT_HEIGHT


class Layout:

    def __init__(self, window: Tk, width: int, height: int):
        self.canvas = Canvas(window, width=width, height=height)

    def draw(self):
        self.canvas.pack()

    def get_canvas(self) -> Canvas:
        return self.canvas


Position = namedtuple("Position", ["x", "y"])


class Bar:

    def __init__(
            self,
            layout: Layout,
            colour: str,
            width: int,
            height: int,
            position: Position = Position(0, 0),
            on_click=None
    ):
        self.canvas = layout.get_canvas()
        self.bar = None
        self.colour = colour
        self.width = width
        self.height = height
        self.position = position
        self.on_click = on_click

    def get_width(self) -> int:
        return self.width

    def set_width(self, width):
        self.width = width

    def get_position(self) -> Position:
        return self.position

    def set_position(self, position: Position):
        self.position = position

    def update(self):
        x0 = self.position.x
        y0 = self.position.y

        self.canvas.coords(
            self.bar,
            x0,
            y0,
            x0 + self.width,
            y0 + self.height
        )

    def set_on_click(self, on_click):
        self.on_click = on_click

    def draw(self):
        x0 = self.position.x
        y0 = self.position.y

        self.bar = self.canvas.create_rectangle(
            x0,
            y0,
            x0 + self.width,
            y0 + self.height,
            fill=self.colour
        )
        self.position = Position(x0, y0)
        self.canvas.tag_bind(self.bar, "<ButtonPress-1>", self.on_click)

    def destroy(self):
        self.canvas.delete(self.bar)


class Experiment:

    def __init__(
            self,
            num_reps: int,
            distances: list,
            widths: list,
            layout_width: int,
            bar_green: Bar,
            bar_blue: Bar,
            participant: str,
            log_filename: str
    ):
        if num_reps < 1:
            raise ValueError("Number of reps must be positive")

        self.num_reps = num_reps
        self.current_rep = 0
        self.distance_width_pairs = self.__get_distance_width_pairs(distances, widths)
        self.current_distance = None
        self.current_width = None
        self.layout_width = layout_width
        self.bar_green = bar_green
        self.bar_blue = bar_blue
        self.participant = participant
        self.log_filename = log_filename
        self.start_time = None

    def get_participant(self) -> str:
        return self.participant

    def get_log_filename(self) -> str:
        return self.log_filename

    def start(self):
        if self.current_rep == 0:
            self.__clear_log_file()
            self.__next_distance_and_width()
            self.__change_positions()
            self.start_time = time.time()

    def next_rep(self) -> bool:
        total_time = (time.time() - self.start_time) * 1000
        current_rep = self.current_rep
        distance = self.current_distance
        width = self.current_width
        has_next = False

        if current_rep in range(0, self.num_reps - 1):
            self.__swap_positions()
            current_rep += 1
            has_next = True
        elif self.distance_width_pairs:
            self.__next_distance_and_width()
            self.__change_positions()
            current_rep = 0
            has_next = True

        self.__log_time(total_time, distance, width)
        self.current_rep = current_rep
        self.start_time = time.time()
        return has_next

    def __next_distance_and_width(self):
        self.current_distance, self.current_width = self.distance_width_pairs.pop()
        return self.current_distance, self.current_rep

    def __swap_positions(self):
        new_green_position = self.bar_blue.get_position()
        new_blue_position = self.bar_green.get_position()

        self.bar_green.set_position(new_green_position)
        self.bar_blue.set_position(new_blue_position)
        self.bar_green.update()
        self.bar_blue.update()

    def __change_positions(self):
        span = self.current_distance + self.current_width
        margin = (self.layout_width - span) / 2

        bar_green_position = Position(margin, self.bar_green.get_position().y)
        bar_blue_position = Position(margin + self.current_distance, self.bar_blue.get_position().y)

        self.bar_green.set_position(bar_green_position)
        self.bar_blue.set_position(bar_blue_position)
        self.bar_green.set_width(self.current_width)
        self.bar_blue.set_width(self.current_width)

        self.bar_green.update()
        self.bar_blue.update()

    def __clear_log_file(self):
        if os.path.exists(self.log_filename):
            os.remove(self.log_filename)

    def __log_time(self, total_time, distance, width):
        with open(self.log_filename, "a", newline="") as file:
            writer = csv.writer(file, delimiter=" ")
            writer.writerow([
                self.participant,
                distance,
                width,
                self.current_rep + 1,
                total_time
            ])

    def __get_distance_width_pairs(self, distances, widths) -> list:
        pairs = list(product(distances, widths))
        shuffle(pairs)
        return pairs


class Controller:

    def __init__(self, window: Tk, layout: Layout, experiment: Experiment, bar_green: Bar, bar_blue: Bar):
        self.window = window
        self.layout = layout
        self.experiment = experiment
        self.bar_green = bar_green
        self.bar_blue = bar_blue

    def start(self):
        self.bar_green.set_on_click(self.on_click)
        self.bar_green.draw()
        self.bar_blue.draw()
        self.layout.draw()

        self.experiment.start()
        self.window.mainloop()

    def __end(self):
        self.bar_green.destroy()
        self.bar_blue.destroy()

        ending_text = f"""
        Thanks a lot for your time {self.experiment.get_participant()}. 
        Hope you found it both fun and challenging!

        Your results have been logged in the file:
        "{self.experiment.get_log_filename()}"

        Made with love by Gift.
        """

        width = self.layout.canvas.winfo_width() / 2
        height = self.layout.canvas.winfo_height() / 2
        font = ("default", 20)
        self.layout.canvas.create_text(width, height, text=ending_text, font=font)

    def on_click(self, _):
        if not self.experiment.next_rep():
            self.__end()


def main():
    window = Tk()
    layout = Layout(window, LAYOUT_WIDTH, LAYOUT_HEIGHT)
    bar_green = Bar(layout, "green", BAR_WIDTH, BAR_HEIGHT)
    bar_blue = Bar(layout, "blue", BAR_WIDTH, BAR_HEIGHT)
    experiment = Experiment(
        NUM_REPS,
        DISTANCES,
        WIDTHS,
        LAYOUT_WIDTH,
        bar_green,
        bar_blue,
        NAME,
        LOG_FILENAME
    )
    controller = Controller(window, layout, experiment, bar_green, bar_blue)
    controller.start()


if __name__ == "__main__":
    main()
