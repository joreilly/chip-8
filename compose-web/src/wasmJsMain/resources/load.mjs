import { instantiate } from './chip8.uninstantiated.mjs';

await wasmSetup;

instantiate({ skia: Module['asm'] });
