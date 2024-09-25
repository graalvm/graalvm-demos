// GraalJS doesn't support TextEncoder yet. It's easy to add and here's a polyfill in the meantime.
import 'fast-text-encoding';

export * as QRCode from 'qrcode'; // qrcode/lib/server.js
