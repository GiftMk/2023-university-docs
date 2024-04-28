from abc import ABC, abstractmethod


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