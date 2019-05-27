# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

## [1.2] - 2019-05-27
- Update `SongDetail` and `DifficultyDetail` models to be more flexible with new field additions

## [1.1] - 2018-10-10
### Added
- Failed downloads are now retried on each scrape

### Changed
- Failed downloads no longer stop the entire scrape
- Logging lines have be changed to be less verbose by default
- Storage format has been changed from a map to a list

## [1.0] - 2018-09-16
### Added
- `scrape` command that scrapes [BeatSaver](https://beatsaver.com/)
- Record file that keeps track of which songs have been scraped