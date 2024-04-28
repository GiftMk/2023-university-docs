from abc import ABC, abstractmethod


class AbstractGame(ABC):

    @abstractmethod
    def launch(self):
        pass
