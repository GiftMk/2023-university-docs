import csv
from math import log2


def get_rows(filename="experiment_log.csv"):
    with open(filename, "r") as file:
        rows = [row[0].split()[1:] for row in csv.reader(file)]
        rows_dict = {}

        for row in rows:
            amplitude_str, width_str, trial_str, time_ms_str = row

            amplitude = float(amplitude_str)
            width = float(width_str)
            trial = float(trial_str)
            time_ms = float(time_ms_str)

            if trial not in [1, 2]:
                key = (amplitude, width, trial)
                time_s = time_ms / 1000
                rows_dict[key] = time_s

        return rows_dict


def get_averages(rows):
    values_dict = {}

    for key, value in rows.items():
        values_key = key[:2]
        if values_key not in values_dict:
            values_dict[values_key] = [value]
        else:
            values_dict[values_key].append(value)

    return {key: sum(value) / len(value) for key, value in values_dict.items()}


def get_index_of_difficulty(amplitude, width):
    return log2(amplitude / width + 1)


def get_summary(averages_dict):
    values_dict = {}

    for key, mean in averages_dict.items():
        amplitude, width = key
        index_of_difficulty = get_index_of_difficulty(amplitude, width)

        if index_of_difficulty not in values_dict:
            values_dict[index_of_difficulty] = [mean]
        else:
            values_dict[index_of_difficulty].append(mean)

    return {key: sum(value) / len(value) for key, value in values_dict.items()}


def write_summary(summaries_dict, header, output_filename="summary.csv"):
    with open(output_filename, "w", newline="") as file:
        writer = csv.writer(file)
        writer.writerow(header)

        for row in sorted(summaries_dict.items()):
            writer.writerow(row)


def main():
    rows_dict = get_rows()
    averages_dict = get_averages(rows_dict)
    summaries_dict = get_summary(averages_dict)
    header = ['ID', 'mean time']
    write_summary(summaries_dict, header)


if __name__ == "__main__":
    main()
