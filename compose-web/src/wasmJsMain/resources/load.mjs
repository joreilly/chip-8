import { instantiate } from './Chip8.uninstantiated.mjs';

await wasmSetup;

instantiate({ skia: Module['asm'] });
